package com.bank.bpm.partners.workers.onboarding.order;

public interface UseCase<R, I> {


	R execute(I in);
}
