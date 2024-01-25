package net.ktccenter.campusApi.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data @NoArgsConstructor @AllArgsConstructor
public class PaymentErrorResponse {
   // private Date timestamp;
    private String response;
    private Integer errorCode;
    private String message;



    public PaymentErrorResponse(Integer errorCode) {
        this.response = "error";
        this.errorCode =  errorCode;
        this.message = getError(errorCode);
    }


    public PaymentErrorResponse(String message) {
        this.response = "error";
        this.errorCode =  500;
        this.message = message;
    }

    public String getError(Integer errorCode){
        Map<Integer, String>codes = new HashMap<>();
        codes.put(70, "some required fields are not specified");
        codes.put(71, "Your amount is not numeric value");
        codes.put(72, "Currency code incorrect, specified a correct currency");
        codes.put(73, "incorrect amount range for specified currency");
        codes.put(74, "email invalid, please provide valid email or leave empty");
        codes.put(75, "public_key not found");
        codes.put(76, "Something wrong, please contact administrator");
        codes.put(77, "item ref already use");
        codes.put(78, "");
        codes.put(79, "");
        return codes.get(errorCode);
    }


}
