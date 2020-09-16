package com.markus.MassMailer.api;

import com.markus.MassMailer.model.data.MailReferenceRepository;
import com.markus.MassMailer.model.mail.MailReference;
import com.markus.MassMailer.model.mail.MassMail;
import com.markus.MassMailer.service.CreateMailerService;
import com.sun.istack.NotNull;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RequestMapping("mailings")
@RestController
public class MassMailController {
    @Autowired
    private MailReferenceRepository mailReferenceRepository;

    @CrossOrigin()
    @PostMapping("send")
    public ApiResponse sendMail(@NotNull @RequestBody String pBody){
        MassMail mailer;

        MailReference mailReference = new MailReference(MailReference.Status.INITIALIZED);
        mailReferenceRepository.save(mailReference);

        try {
            mailer = new CreateMailerService(pBody, mailReference).createMailer();
            mailer.setReferenceRepostory(mailReferenceRepository);
        } catch (JSONException e) {
           mailReferenceRepository.delete(mailReference);
           return new ApiError(
                   HttpStatus.BAD_REQUEST,
                   "Error reading JSON",
                   e.getMessage());
        }

        Thread thread = new Thread(mailer);
        thread.start();
        return new ApiResponse(mailReference);
    }

    @CrossOrigin()
    @GetMapping("get_status/{id}")
    MailReference getStatus(@PathVariable Long id){
        return mailReferenceRepository.findById(id)
            .orElseThrow(() -> new MailReferenceNotFoundException(id));
    }
}
