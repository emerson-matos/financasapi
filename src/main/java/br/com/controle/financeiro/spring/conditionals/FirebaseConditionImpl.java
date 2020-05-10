package br.com.controle.financeiro.spring.conditionals;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class FirebaseConditionImpl implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		String enabled = context.getEnvironment().getProperty("br.com.controle.financeiro.firebase.enabled");
		return Boolean.parseBoolean(enabled);
	}
}