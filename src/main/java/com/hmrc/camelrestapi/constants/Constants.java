package com.hmrc.camelrestapi.constants;

public enum Constants {
    INTERNAL_SERVER_ERROR("Error processing the request, please try later or contact administrator"),
    BAD_REQUEST_ERROR("Bad Request, please send a valid xml request");

    private final String text;

    Constants(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
