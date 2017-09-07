<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String context=request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
	<head>
	    <!-- Required meta tags -->
	    <meta charset="utf-8">
	    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	    <link href="<%=context%>/static/css/common.css" rel="stylesheet"/>
	    <!-- Bootstrap CSS -->
	    <link rel="stylesheet" href="<%=context%>/static/assets/css/bootstrap3.3.7min.css">
	    <link href="<%=context%>/static/css/definded.css" rel="stylesheet"/>
	    <link href="<%=context%>/static/css/seller_center.css" rel="stylesheet"/>
    </head>
	<body style="background-image: url(http://static.belimaterial.com/static/2017/7/4/703f078b54e14236add338e899794f38.jpg);background-repeat: no-repeat;background-size:100% auto;"> 
	    <input type="hidden" id="rootsite" value="<%=context %>">
	    <div class="container" >
		    <!--title  -->
		    <div class="col-lg-4" style="margin-top:12%;margin-left:15%;color:white">
			    <h1 style="font-size:5em">Sign</h1>
			    <h2>Welcome to Beli Material</h2>
			</div>
	        <!--表单面板  -->
	    	<div class="col-lg-4 panel panel-default" style="margin-top:12%;margin-left:15%;background-color: white;border-radius:2%;" >
				<div class="panel-body" style="padding-bottom:15px" >
					<form action="<%=context%>/loginRegister/vendorlogin" method="post" role="form" id="form">              
						<h2>Sign in.</h2>
		                <hr>
		                <div class="form-group">
		                    <label class="control-label" for="memberAccount">Username</label>
		                    <input class="form-control"  required name="account" id="memberAccount" maxlength="20" type="text" >
		                </div>
		                <div class="form-group">
		                    <label class="control-label" for="memberPwd">Password</label>
		                 <div class="input-group">
		                        <input class="form-control"  required  name="password" id="memberPwd" maxlength="30" type="password">
		                      <div class="input-group-btn">
		                      <button type="button" class="btn btn-default" aria-label="Left Align" id="glyphicon-eye-open">
		                      <span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
		                </button>
		                        </div>
		                    </div>
		                </div>
		                <div class="form-group">
		                    <label class="control-label" for="Password">Verification Code</label>
		                      <div class="input-group">
		                         <input class="form-control"  name="verificationCode" type="text" required maxlength="4" minlength="4">
		                      <div class="input-group-btn">
		                       <img src="<%=context%>/loginRegister/code" style="width:70px;height:33px;margin-left:5%" id="imagecode">
		                         <button class="btn text-info"  type="button" style="border-width: 0px;background-color: white;" id="change">Change</button>
		                        </div>
		                    </div>
		                </div>
		                <div class="form-group" >
		                    <div>
		                        <input type="submit" value="Login" class="btn btn-lg btn-primary">
		                    </div>
		                </div>
					</form>     
				</div> 
   			 </div>
    	</div>

      	<div class="container text-center">
	        <p style="margin-top:10%;color:white">
	        *Recommend the use of IE8 above or Chrome browser to access the site.
	        </p>
		</div>
 


   
    <!--jQuery first, then Tether, then Bootstrap JS.-->
    <script type="text/javascript" src="<%=context%>/static/assets/js/jquery-3.1.1.min.js"></script>
    <script src="<%=context%>/static/assets/js/jquery.validate.min.js"></script>
    <script src="<%=context%>/static/assets/js/tether.min.js" ></script>
    <script src="<%=context%>/static/assets/js/bootstrap3.3.7.min.js"></script>
    <script src="<%=context%>/static/js/definded.js"></script>
    <script src="<%=context%>/static/js/login.js"></script>
	</body>
</html>