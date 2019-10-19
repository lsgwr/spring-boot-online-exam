/***********************************************************
 * @Description : 
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019-05-17 07:50
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.exception;

import com.huawei.l00379880.exam.enums.ResultEnum;
import lombok.Getter;

@Getter
public class ExamException extends RuntimeException {
    private Integer code;

    public ExamException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public ExamException( Integer code, String message) {
        super(message);
        this.code = code;
    }
}
