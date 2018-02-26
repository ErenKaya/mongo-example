package kim.eren.infonalquest.controller;


import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import kim.eren.infonalquest.model.Response;
import kim.eren.infonalquest.model.User;
import kim.eren.infonalquest.service.UserService;
import kim.eren.infonalquest.utils.ReCaptchaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    UserService userService;


    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseEntity<?> addUser(
            @Valid
            @RequestBody User user,
            HttpServletRequest request) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber phonenumber;

        String ip = request.getRemoteAddr();
        String captchaVerifyMessage =
                userService.verifyRecaptcha(ip, user.getCaptcha());

        if (!captchaVerifyMessage.equals(ReCaptchaUtil.SUCCESS)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", captchaVerifyMessage);
            return new ResponseEntity<>(response.get("message"),HttpStatus.BAD_REQUEST);
        }

        try{
           phonenumber = phoneNumberUtil.parse(user.getPhoneNumber(),"TR");
           boolean isValid = phoneNumberUtil.isValidNumber(phonenumber);
           if(isValid){
               user.setPhoneNumber(String.valueOf(phonenumber.getNationalNumber()));
               userService.addUser(user);
               return new ResponseEntity<>(new Response(HttpStatus.OK.toString()), HttpStatus.OK);


           }else{
               return new ResponseEntity<>("Unsupported phone number",HttpStatus.UNSUPPORTED_MEDIA_TYPE);
           }
        }catch (NumberParseException ex){
            return new ResponseEntity<>("Unsupported phone number character",HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }


    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user){
        User tempUser = userService.find(user.getId());
        if(tempUser==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            userService.updateUser(user);
            return new ResponseEntity<>(new Response(HttpStatus.OK.toString()), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ResponseEntity<?> listAllUser() {
        return new ResponseEntity<>(userService.listAllUser(),HttpStatus.OK);
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        User user = userService.find(id);
        if (user == null) {
            return new ResponseEntity<>("User Not Found",HttpStatus.NOT_FOUND);
        } else {
            userService.deleteUser(id);
            return new ResponseEntity<>(new Response(HttpStatus.OK.toString()), HttpStatus.OK);
        }
    }

}

