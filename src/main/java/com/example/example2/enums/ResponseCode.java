package com.example.example2.enums;

public enum ResponseCode {
    CERO_CERO("00", "Exito",
            "Exito",
            "Se ha eliminado la tarjeta",
            "Compra exitosa",
            "Compra anulada"),
    CERO_ONE("01", "Fallido",
            "Tarjeta no existe",
            "No se ha eliminado la tarjeta",
            "Tarjeta no existe",
            "Numero de referencia inválido"
    ),
    CERO_TWO("02", "Tajeta ya existe",
            "Número de validación inválido",
            "Tarjeta no existe",
            "Tarjeta no enrrolada",
            "No se puede anular transacción");

    private final String code;
    private final String createMethodMessage;
    private final String enrollMethodMessage;
    private final String deleteMethodMessage;
    private final String createTransactionMethodMessage;
    private final String cancelTransactionMethodMessage;

    ResponseCode(String code, String createMethodMessage, String enrollMethodMessage, String deleteMethodMessage, String createTransactionMethodMessage, String cancelTransactionMethodMessage) {
        this.code = code;
        this.createMethodMessage = createMethodMessage;
        this.enrollMethodMessage = enrollMethodMessage;
        this.deleteMethodMessage = deleteMethodMessage;
        this.createTransactionMethodMessage = createTransactionMethodMessage;
        this.cancelTransactionMethodMessage = cancelTransactionMethodMessage;
    }

    public String getCode() {
        return code;
    }

    public String getCreateMethodMessage() {
        return createMethodMessage;
    }

    public String getEnrollMethodMessage() {
        return enrollMethodMessage;
    }

    public String getDeleteMethodMessage() {
        return deleteMethodMessage;
    }

    public String getCreateTransactionMethodMessage() {
        return createTransactionMethodMessage;
    }

    public String getCancelTransactionMethodMessage() {
        return cancelTransactionMethodMessage;
    }
}
