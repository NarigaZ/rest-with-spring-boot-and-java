package br.com.narigaz.restwithspringbootandjava.exceptions;

import java.io.Serializable;
import java.util.Date;

public record ExceptionResponse(Date timestamp, String message, String details) implements Serializable {}
