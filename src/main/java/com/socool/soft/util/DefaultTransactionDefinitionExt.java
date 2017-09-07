package com.socool.soft.util;

import org.springframework.stereotype.Component;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@SuppressWarnings("serial")
@Component
public class DefaultTransactionDefinitionExt extends
		DefaultTransactionDefinition {

	public DefaultTransactionDefinitionExt() {
		super();
		super.setPropagationBehaviorName("PROPAGATION_REQUIRES_NEW");
	}

	
}
