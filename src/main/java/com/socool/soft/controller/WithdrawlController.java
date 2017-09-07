package com.socool.soft.controller;

import com.socool.soft.bo.*;
import com.socool.soft.bo.constant.MerchantWidStatusEnum;
import com.socool.soft.bo.constant.OrderFeeTypeEnum;
import com.socool.soft.bo.constant.OrderOperationTypeEnum;
import com.socool.soft.service.*;
import com.socool.soft.util.ExportUtil;
import com.socool.soft.vo.Page;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/withdrawl")
public class WithdrawlController extends BaseController {

    @Autowired
    private IMerchantWidService merchantWidService;

    @Autowired
    private IOrderService orderService;
	@Autowired
	private IOrderFeeService orderFeeService;

    @Autowired
    private IMerchantService merchantService;

/*	@Autowired
	private SerialGeneratorService serialGeneratorService;*/

    @Autowired
    private IBuyerService buyerService;
    @Autowired
    private IMerchantUserService merchantUserService;
    @Autowired
    private IProdService prodService;
    @Autowired
    private IOrderProdSkuPropInfoService orderProdSkuPropInfoService;
    @Autowired
    private IOrderProdService orderProdService;
    @Autowired
    private IOrderOperationService orderOperationService;
    @Autowired
    private IOrderConsigneeService orderConsigneeService;

    @RequestMapping(value = "/list")
    public String list(HttpServletRequest request,
                       RcMerchantWid merchantWid,
                       Model model,
                       Integer currentPage) {
        Integer merchantUserId = getMerchantUserId(request);
        if (merchantUserId != null) {
            RcMerchantUser merchantUser = merchantUserService.findMerchantUserById(merchantUserId);
            merchantWid.setMerchantId(merchantUser.getMerchantId());
        }
        boolean flag = false;
        if (currentPage == null) {
            currentPage = 1;
            flag = true;
        }
        // 分页
        Page page = new Page();
        // 初始化时第一页
        page.setPagination(true);
        page.setPageSize(10);
        page.setCurrentPage(currentPage);
        model.addAttribute("page", page);
        List<RcMerchantWid> merchantWids = merchantWidService.findPagedMerchantWids(merchantWid, page);
        List<RcMerchant> merchants = merchantService.findAllMerchants();
        model.addAttribute("list", merchantWids);
        model.addAttribute("storeList", merchants);
        if (flag) {
            return "withdrawl/auditWithdrawlList";
        } else {
            return "withdrawl/auditWithdrawlList_inner";
        }
    }

    @RequestMapping(value = "/changeStatus")
    @ResponseBody
    public String changeStatus(long merchantWidId, int status) {
        RcMerchantWid merchantWid = merchantWidService.findMerchantWidById(merchantWidId);
        if (merchantWid == null) {
            return "{\"code\":\"0\"}";
        }
        merchantWid.setStatus(status);
        merchantWidService.modifyMerchantWid(merchantWid);
        return "{\"code\":\"1\"}";
    }

    @RequestMapping(value = "/workflow")
    @ResponseBody
    public String workflow(Integer merchantId, String calTimeStr) throws ParseException {
        if (merchantId == null) {
            List<RcMerchant> merchants = merchantService.findAllEnabledMerchants();
            for (RcMerchant merchant : merchants) {
                RcMerchantWid param = new RcMerchantWid();
                param.setMerchantId(merchant.getMerchantId());
                param.setBalanceMonth(calTimeStr);
                RcMerchantWid merchantWid = merchantWidService.findMerchantWid(param);
                if (merchantWid != null && merchantWid.getStatus() != MerchantWidStatusEnum.PENDING.getValue() && merchantWid.getStatus() != MerchantWidStatusEnum.UNCONFIRMED.getValue()) {
                    continue;
                }

                DateFormat format = new SimpleDateFormat("yyyy-MM");
                Date createTimeFrom = format.parse(calTimeStr);
                Calendar cal = Calendar.getInstance();
                cal.setTime(createTimeFrom);
                cal.add(Calendar.MONTH, 1);
                cal.add(Calendar.SECOND, -1);
                Date createTimeTo = cal.getTime();
                BigDecimal applyAmount = orderService.statisticOrderPayPrice(merchant.getMerchantId(), createTimeFrom, createTimeTo);

                if (merchantWid != null) {
                    merchantWid.setApplyAmount(applyAmount);
                    merchantWid.setBankName(merchant.getBankName());
                    merchantWid.setBankAccount(merchant.getBankAccount());
                    merchantWid.setBankAccountName(merchant.getBankAccountName());
                    merchantWid.setStatus(MerchantWidStatusEnum.PENDING.getValue());
                    merchantWid.setApplyTime(new Date());
                    merchantWidService.modifyMerchantWid(merchantWid);
                } else {
                    merchantWid = new RcMerchantWid();
                    merchantWid.setMerchantId(merchant.getMerchantId());
                    merchantWid.setApplyAmount(applyAmount);
                    merchantWid.setBankName(merchant.getBankName());
                    merchantWid.setBankAccount(merchant.getBankAccount());
                    merchantWid.setBankAccountName(merchant.getBankAccountName());
                    merchantWid.setStatus(MerchantWidStatusEnum.PENDING.getValue());
                    merchantWid.setApplyTime(new Date());
                    merchantWid.setBalanceMonth(calTimeStr);
                    merchantWidService.addMerchantWid(merchantWid);
                }
            }
        } else {
            RcMerchant merchant = merchantService.findMerchantById(merchantId);
            RcMerchantWid param = new RcMerchantWid();
            param.setMerchantId(merchantId);
            param.setBalanceMonth(calTimeStr);
            RcMerchantWid merchantWid = merchantWidService.findMerchantWid(param);
            if (merchantWid != null && merchantWid.getStatus() != MerchantWidStatusEnum.PENDING.getValue() && merchantWid.getStatus() != MerchantWidStatusEnum.UNCONFIRMED.getValue()) {
                return "{\"code\":\"1\"}";
            }

            DateFormat format = new SimpleDateFormat("yyyy-MM");
            Date createTimeFrom = format.parse(calTimeStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(createTimeFrom);
            cal.add(Calendar.MONTH, 1);
            cal.add(Calendar.SECOND, -1);
            Date createTimeTo = cal.getTime();
            BigDecimal applyAmount = orderService.statisticOrderPayPrice(merchant.getMerchantId(), createTimeFrom, createTimeTo);

            if (merchantWid != null) {
                merchantWid.setApplyAmount(applyAmount);
                merchantWid.setBankName(merchant.getBankName());
                merchantWid.setBankAccount(merchant.getBankAccount());
                merchantWid.setBankAccountName(merchant.getBankAccountName());
                merchantWid.setStatus(MerchantWidStatusEnum.PENDING.getValue());
                merchantWid.setApplyTime(new Date());
                merchantWidService.modifyMerchantWid(merchantWid);
            } else {
                merchantWid = new RcMerchantWid();
                merchantWid.setMerchantId(merchant.getMerchantId());
                merchantWid.setApplyAmount(applyAmount);
                merchantWid.setBankName(merchant.getBankName());
                merchantWid.setBankAccount(merchant.getBankAccount());
                merchantWid.setBankAccountName(merchant.getBankAccountName());
                merchantWid.setStatus(MerchantWidStatusEnum.PENDING.getValue());
                merchantWid.setApplyTime(new Date());
                merchantWid.setBalanceMonth(calTimeStr);
                merchantWidService.addMerchantWid(merchantWid);
            }
        }
        return "{\"code\":\"1\"}";
    }

    @RequestMapping(value = "/orderHistory")
    public String orderHistory(HttpServletResponse response, String dateStr, int merchantId, Model model, Integer currentPage) throws ParseException, IOException {
        boolean flag = false;
        if (currentPage == null) {
            currentPage = 1;
            flag = true;
        }
        // 分页
        Page page = new Page();
        // 初始化时第一页
        page.setPagination(true);
        page.setPageSize(10);
        page.setCurrentPage(currentPage);
        model.addAttribute("page", page);

        DateFormat format = new SimpleDateFormat("yyyy-MM");
        Date createTimeFrom = format.parse(dateStr);
        Calendar cal = Calendar.getInstance();
        cal.setTime(createTimeFrom);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.SECOND, -1);
        Date createTimeTo = cal.getTime();
        List<RcOrder> orders = orderService.findPagedOrderHistories(merchantId, createTimeFrom, createTimeTo, page);
        for (RcOrder order : orders) {
            order.setMember(buyerService.findBuyerById(order.getMemberId()));
            order.setMerchant(merchantService.findMerchantById(order.getMerchantId()));
            order.setProductPrice(new BigDecimal(0));
            List<RcOrderProd> orderProds = orderProdService.findOrderProdsByOrderId(order.getOrderId());
            for (RcOrderProd orderProd : orderProds) {
                order.setProductPrice(order.getProductPrice().add(orderProd.getAmount()));
            }
            List<RcOrderOperation> orderOperations = orderOperationService.findOrderOperationsByOrderId(order.getOrderId());
            for (RcOrderOperation orderOperation : orderOperations) {
                if (orderOperation.getOperationType() == OrderOperationTypeEnum.RECEIVE.getValue() ||
                        orderOperation.getOperationType() == OrderOperationTypeEnum.PICKUP_CONFIRM.getValue()) {
                    order.setReceiveTime(orderOperation.getOperationTime());
                }
            }
        }
        model.addAttribute("list", orders);
        if (flag) {
            return "withdrawl/orderBox";
        } else {
            return "withdrawl/orderBox_inner";
        }
    }

    @RequestMapping(value = "/orderHistoryExcel")
    public void orderHistoryExcel(HttpServletRequest request, HttpServletResponse response, String dateStr, Integer merchantId) throws ParseException, IOException {
        DateFormat format = new SimpleDateFormat("yyyy-MM");
        Date createTimeFrom = format.parse(dateStr);
        Calendar cal = Calendar.getInstance();
        cal.setTime(createTimeFrom);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.SECOND, -1);
        Date createTimeTo = cal.getTime();
        List<RcOrder> orders = orderService.findOrderHistories(merchantId, createTimeFrom, createTimeTo);
        for (RcOrder order : orders) {
            order.setMember(buyerService.findBuyerById(order.getMemberId()));
            order.setMerchant(merchantService.findMerchantById(order.getMerchantId()));
            order.setProductPrice(new BigDecimal(0));
            List<RcOrderProd> orderProds = orderProdService.findOrderProdsByOrderId(order.getOrderId());
            for (RcOrderProd orderProd : orderProds) {
                order.setProductPrice(order.getProductPrice().add(orderProd.getAmount()));
                orderProd.setProd(prodService.findProdById(orderProd.getProdId()));
                orderProd.setOrderProdSkuPropInfos(orderProdSkuPropInfoService.findOrderProdSkuPropInfosByOrderProdId(orderProd.getProdId()));
            }
            order.setOrderProds(orderProds);
            List<RcOrderOperation> orderOperations = orderOperationService.findOrderOperationsByOrderId(order.getOrderId());
            for (RcOrderOperation orderOperation : orderOperations) {
                if (orderOperation.getOperationType() == OrderOperationTypeEnum.RECEIVE.getValue() ||
                        orderOperation.getOperationType() == OrderOperationTypeEnum.PICKUP_CONFIRM.getValue()) {
                    order.setReceiveTime(orderOperation.getOperationTime());
                }
            }
            RcOrderConsignee orderConsignee = orderConsigneeService.findOrderConsigneeById(order.getOrderId());
            order.setOrderConsignee(orderConsignee);
            order.setCouponAmount(new BigDecimal(0));
			order.setFreight(new BigDecimal(0));
			order.setCreditCardFee(new BigDecimal(0));
			List<RcOrderFee> orderFees = orderFeeService.findOrderFeesByOrderId(order.getOrderId());
			for(RcOrderFee orderFee : orderFees) {
				if(orderFee.getType() == OrderFeeTypeEnum.COUPON.getValue()) {
					order.setCouponAmount(orderFee.getAmount());
				}
				if(orderFee.getType() == OrderFeeTypeEnum.FREIGHT.getValue()) {
					order.setFreight(orderFee.getAmount());
				}
				if(orderFee.getType() == OrderFeeTypeEnum.CREDIT_CARD_FEE.getValue()) {
					order.setCreditCardFee(orderFee.getAmount());
				}
			}
        }
        createExcel(request, response, orders);
    }

    private void createExcel(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                             List<RcOrder> rcOrders) throws IOException {
        String[] title = {"Order ID", "Order Time", "Order Price", "Discount", "Freight", "Pay Amount", "Store ID",
                "Store", "Buyer ID", "Buyer Username", "Buyer Phone", "Product", "Consignee Name", "Consignee Phone",
                "Consignee Address"};
        // 创建Excel工作簿

        Workbook workbook = new SXSSFWorkbook(100);
        // 创建一个工作表sheet
        Sheet sheet = workbook.createSheet();
        // 创建第一行
        Row row = sheet.createRow(0);

        ExportUtil exportUtil = new ExportUtil(workbook, sheet);
        CellStyle head = exportUtil.getHeadStyle();
        CellStyle body = exportUtil.getBodyStyle();
        // 插入第一行数据 id,name,sex
        for (int i = 0; i < title.length; i++) {
            sheet.setColumnWidth(i, 30 * 256);
            Cell cell = row.createCell(i);
            cell.setCellStyle(head);
            cell.setCellValue(title[i]);
        }
        // 追加数据
        for (int i = 1; i <= rcOrders.size(); i++) {
            Row rownext = sheet.createRow(i);
            RcOrder rcOrderVo = rcOrders.get(i - 1);
            Cell cell = rownext.createCell(0);
            cell.setCellStyle(body);
            cell.setCellValue(rcOrderVo.getOrderId());
            cell = rownext.createCell(1);
            cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(rcOrderVo.getCreateTime()));
            cell = rownext.createCell(2);
            cell.setCellValue(rcOrderVo.getProductPrice().doubleValue());
            cell = rownext.createCell(3);
            cell.setCellValue(rcOrderVo.getProductPrice().add(rcOrderVo.getFreight())
                    .subtract(rcOrderVo.getPayPrice()).doubleValue());
            cell = rownext.createCell(4);
            cell.setCellValue(rcOrderVo.getFreight().doubleValue());
            cell = rownext.createCell(5);
            cell.setCellValue(rcOrderVo.getPayPrice().doubleValue());
            cell = rownext.createCell(6);
            cell.setCellValue(rcOrderVo.getMerchantId());
            cell = rownext.createCell(7);
            cell.setCellValue(rcOrderVo.getMerchant().getName());
            cell = rownext.createCell(8);
            cell.setCellValue(rcOrderVo.getMemberId());
            cell = rownext.createCell(9);
            cell.setCellValue(rcOrderVo.getMember().getName());
            cell = rownext.createCell(10);
            cell.setCellValue(rcOrderVo.getOrderConsignee().getMobile());
            cell = rownext.createCell(11);
            List<RcOrderProd> rcOrderProds = rcOrderVo.getOrderProds();
            StringBuffer sb = new StringBuffer("[ ");
            for (RcOrderProd rcOrderProd : rcOrderProds) {
                sb.append(rcOrderProd.getProd().getName());
                List<RcOrderProdSkuPropInfo> List = rcOrderProd.getOrderProdSkuPropInfos();
                for (RcOrderProdSkuPropInfo rcProdSkuPropInfo : List) {
                    sb.append("_").append(rcProdSkuPropInfo.getProdPropVal());
                }
				/*
				 * String prop =
				 * StringUtils.join(rcOrderProd.getRcProdSkuPropInfos(), "_");
				 * sb.append(prop);
				 */
                sb.append(" ]").append(" x ").append(rcOrderProd.getQuantity() + ";");
            }
            cell.setCellValue(sb.toString());
            cell = rownext.createCell(12);
            cell.setCellValue(rcOrderVo.getOrderConsignee().getName());
            cell = rownext.createCell(13);
            cell.setCellValue(rcOrderVo.getOrderConsignee().getMobile());
            cell = rownext.createCell(14);
            cell.setCellValue(rcOrderVo.getOrderConsignee().getAddress());
        }
        httpServletResponse.setHeader("Content-disposition", "attachment; filename=orderExcel.xlsx");// 组装附件名称和格式
        workbook.write(httpServletResponse.getOutputStream());
        IOUtils.closeQuietly(httpServletResponse.getOutputStream());

    }

    @RequestMapping(value = "/approved")
    @ResponseBody
    public String approved(HttpServletRequest request, int buzId) {
        RcMerchantWid merchantWid = merchantWidService.findMerchantWidById(buzId);
        if (merchantWid == null) {
            return "{\"code\":\"0\"}";
        }
        merchantWid.setProcessTime(new Date());
        merchantWid.setStatus(MerchantWidStatusEnum.CONFIRMED.getValue());
        merchantWidService.modifyMerchantWid(merchantWid);

        return "{\"code\":\"1\"}";
    }

    @RequestMapping(value = "/unapproved")
    @ResponseBody
    public String unapproved(int buzId) {
        RcMerchantWid merchantWid = merchantWidService.findMerchantWidById(buzId);
        if (merchantWid == null) {
            return "{\"code\":\"0\"}";
        }
        merchantWid.setProcessTime(new Date());
        merchantWid.setStatus(MerchantWidStatusEnum.UNCONFIRMED.getValue());
        merchantWidService.modifyMerchantWid(merchantWid);
        return "{\"code\":\"1\"}";
    }

    @RequestMapping(value = "/paid")
    @ResponseBody
    public String paid(int buzId) {
        RcMerchantWid merchantWid = merchantWidService.findMerchantWidById(buzId);
        if (merchantWid == null) {
            return "{\"code\":\"0\"}";
        }
        merchantWid.setPayTime(new Date());
        merchantWid.setStatus(MerchantWidStatusEnum.PAID.getValue());
        merchantWidService.modifyMerchantWid(merchantWid);
        return "{\"code\":\"1\"}";
    }
}
