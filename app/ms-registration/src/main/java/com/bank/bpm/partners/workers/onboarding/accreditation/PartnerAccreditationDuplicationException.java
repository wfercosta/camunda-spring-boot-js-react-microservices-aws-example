package com.bank.bpm.partners.workers.onboarding.accreditation;

import com.bank.bpm.partners.workers.onboarding.component.BusinessProcessModelException;

import java.text.MessageFormat;

import static com.bank.bpm.partners.workers.onboarding.accreditation.PartnerAccreditationConstants.ERROR_PARTNER_REGISTRATION_DUPLICATED;

public class PartnerAccreditationDuplicationException extends BusinessProcessModelException {

	public PartnerAccreditationDuplicationException(final PartnerAccreditation partnerAccreditation) {
		super(ERROR_PARTNER_REGISTRATION_DUPLICATED,
				MessageFormat.format("Partner with document number {0} is duplicated"
						, partnerAccreditation.getDocumentNumber()));
	}

}
