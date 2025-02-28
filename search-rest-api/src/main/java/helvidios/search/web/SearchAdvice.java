package helvidios.search.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class SearchAdvice {
    
    @ResponseBody
    @ExceptionHandler(SearchException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String searchExceptionHandler(SearchException ex){
        return ex.getMessage();
    }
}