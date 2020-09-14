package com.markus.MassMailer.model.data;

import com.markus.MassMailer.model.mail.MailReference;
import org.springframework.data.repository.CrudRepository;

public interface MailReferenceRepository extends CrudRepository<MailReference, Long> {
    MailReference findById(long id);
}
