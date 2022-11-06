package br.com.narigaz.restwithspringbootandjava.controllers;

import br.com.narigaz.restwithspringbootandjava.exceptions.UnsupportedMathOperationException;
import br.com.narigaz.restwithspringbootandjava.utils.Converter;
import br.com.narigaz.restwithspringbootandjava.utils.SimpleMath;
import org.springframework.web.bind.annotation.*;

@RestController
public class MathController {

    public static final String PLEASE_SET_A_NUMERIC_VALUE = "Please set a numeric value";

    @RequestMapping(value = "/sum/{numberOne}/{numberTwo}", method = RequestMethod.GET)
    public Double sum(@PathVariable(value = "numberOne") String numberOne, @PathVariable(value = "numberTwo") String numberTwo) {
        if (!Converter.isNumeric(numberOne) || !Converter.isNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException(PLEASE_SET_A_NUMERIC_VALUE);
        }
        return SimpleMath.sum(Converter.convertoToDouble(numberOne),Converter.convertoToDouble(numberTwo));
    }

    @RequestMapping(value = "/sub/{numberOne}/{numberTwo}", method = RequestMethod.GET)
    public Double sub(@PathVariable(value = "numberOne") String numberOne, @PathVariable(value = "numberTwo") String numberTwo) {
        if (!Converter.isNumeric(numberOne) || !Converter.isNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException(PLEASE_SET_A_NUMERIC_VALUE);
        }
        return SimpleMath.sub(Converter.convertoToDouble(numberOne),Converter.convertoToDouble(numberTwo));
    }

    @RequestMapping(value = "/mult/{numberOne}/{numberTwo}", method = RequestMethod.GET)
    public Double multiplication(@PathVariable(value = "numberOne") String numberOne, @PathVariable(value = "numberTwo") String numberTwo) {
        if (!Converter.isNumeric(numberOne) || !Converter.isNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException(PLEASE_SET_A_NUMERIC_VALUE);
        }
        return SimpleMath.mult(Converter.convertoToDouble(numberOne),Converter.convertoToDouble(numberTwo));
    }

    @RequestMapping(value = "/div/{numberOne}/{numberTwo}", method = RequestMethod.GET)
    public Double division(@PathVariable(value = "numberOne") String numberOne, @PathVariable(value = "numberTwo") String numberTwo) {
        if (!Converter.isNumeric(numberOne) || !Converter.isNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException(PLEASE_SET_A_NUMERIC_VALUE);
        }
        return SimpleMath.div(Converter.convertoToDouble(numberOne),Converter.convertoToDouble(numberTwo));
    }

    @RequestMapping(value = "/med/{numberOne}/{numberTwo}", method = RequestMethod.GET)
    public Double media(@PathVariable(value = "numberOne") String numberOne, @PathVariable(value = "numberTwo") String numberTwo) {
        if (!Converter.isNumeric(numberOne) || !Converter.isNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException(PLEASE_SET_A_NUMERIC_VALUE);
        }
        return SimpleMath.med(Converter.convertoToDouble(numberOne),Converter.convertoToDouble(numberTwo));
    }

    @RequestMapping(value = "/sqrt/{number}", method = RequestMethod.GET)
    public Double squareRoot(@PathVariable(value = "number") String number) {
        if (!Converter.isNumeric(number)) {
            throw new UnsupportedMathOperationException(PLEASE_SET_A_NUMERIC_VALUE);
        }
        return SimpleMath.sqrt(Converter.convertoToDouble(number));
    }
}
