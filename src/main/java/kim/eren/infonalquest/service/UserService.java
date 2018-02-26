package kim.eren.infonalquest.service;


import kim.eren.infonalquest.model.User;
import kim.eren.infonalquest.repository.UserRepository;
import kim.eren.infonalquest.utils.ReCaptchaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Value("${google.recaptcha.secret}")
    String recaptchaSecret;

    private static final String GOOGLE_RECAPTCHA_VERIFY_URL =
            "https://www.google.com/recaptcha/api/siteverify";

    @Autowired
    UserRepository userRepository;

    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    public void addUser(User user) {
        generateUserId(user);
        userRepository.save(user);

    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(String id){

        userRepository.delete(id);

    }

    public List<User> listAllUser() {
        return userRepository.findAll();
    }

    public User find(String id){
        return userRepository.findOne(id);
    }


    public String verifyRecaptcha(String ip,
                                  String recaptchaResponse) {
        Map<String, String> body = new HashMap<>();
        body.put("secret", recaptchaSecret);
        body.put("response", recaptchaResponse);
        body.put("remoteip", ip);
        ResponseEntity<Map> recaptchaResponseEntity =
                restTemplateBuilder.build()
                        .postForEntity(GOOGLE_RECAPTCHA_VERIFY_URL +
                                        "?secret={secret}&response={response}&remoteip={remoteip}",
                                body, Map.class, body);

        Map<String, Object> responseBody =
                recaptchaResponseEntity.getBody();

        boolean recaptchaSucess = (Boolean) responseBody.get("success");
        if (!recaptchaSucess) {
            List<String> errorCodes =
                    (List) responseBody.get("error-codes");

            String errorMessage = errorCodes.stream()
                    .map(s -> ReCaptchaUtil.RECAPTCHA_ERROR_CODE.get(s))
                    .collect(Collectors.joining(", "));

            return errorMessage;
        } else {
            return ReCaptchaUtil.SUCCESS;
        }
    }

    private void generateUserId(User user) {
        user.setId(user.getName().substring(1)
                + user.getSurname().substring(user.getSurname().length() - 1)
                + System.currentTimeMillis());
    }
}
