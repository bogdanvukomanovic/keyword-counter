package result_retriever.response;

public record Response(ResponseStatus responseStatus, String message, Object content) {

    @Override
    public String toString() {

        if (responseStatus.equals(ResponseStatus.OK)) {
            return content.toString();
        } else if (responseStatus.equals(ResponseStatus.IN_PROGRESS) || responseStatus.equals(ResponseStatus.ERROR)) {
            return message;
        } else {
            return ">> Error: Unexpected value.";
        }

    }

}
