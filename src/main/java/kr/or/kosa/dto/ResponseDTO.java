package kr.or.kosa.dto;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {
    private HttpStatus httpStatus;
    private String message;
    private List<Object> result;
    
    public int getCode() {
    	return httpStatus.value();
    }
}
