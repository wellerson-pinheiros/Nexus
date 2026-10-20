package nexus.com.br.game_store.service;

import nexus.com.br.game_store.excecoes.EmailEnvioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarEmail(String destinatario, String assunto, String mensagem) {

        try{
        SimpleMailMessage email = new SimpleMailMessage();

        // Opcional: defina o remetente (deve ser o mesmo do application.properties)
        email.setFrom("suporte.nexusgamestore@gmail.com");
        email.setTo(destinatario);
        email.setSubject(assunto);
        email.setText(mensagem);

        mailSender.send(email);

    } catch (Exception e) {
        throw new EmailEnvioException("Erro ao enviar e-mail para: " + destinatario, e);
    }
    }

    public void enviarEmailSenhaAlterada(String emailDestinatario) {
        try{
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("seuemail@gmail.com");
        message.setTo(emailDestinatario);
        message.setSubject("Aviso de Segurança: Sua senha foi alterada");
        message.setText("Olá,\n\n" +
                "Informamos que a senha da sua conta foi alterada com sucesso.\n\n" +
                "Se foi você quem fez esta alteração, nenhuma ação é necessária. " +
                "Se você NÃO fez esta alteração, entre em contato imediatamente com o nosso suporte para proteger sua conta.\n\n" +
                "Atenciosamente,\nEquipe GameStore");

        mailSender.send(message);

    } catch (Exception e) {
        throw new EmailEnvioException("Erro ao enviar aviso de alteração de senha para: " + emailDestinatario, e);
    }
    }
}