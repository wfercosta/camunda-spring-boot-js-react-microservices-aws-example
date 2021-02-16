package com.bank.bpm.partners.workers.onboarding.accreditation;

import com.bank.bpm.partners.workers.onboarding.component.BusinessProcessModelException;

import java.text.MessageFormat;

import static com.bank.bpm.partners.workers.onboarding.accreditation.PartnerConstants.ERROR_PARTNER_REGISTRATION_DUPLICATED;

public class PartnerDuplicationException extends BusinessProcessModelException {

	public PartnerDuplicationException(final Partner partner) {
		super(ERROR_PARTNER_REGISTRATION_DUPLICATED,
				MessageFormat.format("Partner with document number {0} is duplicated"
						, partner.getDocumentNumber()));
	}

}
