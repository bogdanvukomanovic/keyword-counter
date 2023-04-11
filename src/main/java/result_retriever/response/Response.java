package result_retriever.response;

public record Response(String responseStatus, String errorMessage, Object content) {

    @Override
    public String toString() {

        if (responseStatus.equals(ResponseStatus.OK)) {
            return content.toString();
        } else {
            return "Error: " + errorMessage;
        }

    }

}
