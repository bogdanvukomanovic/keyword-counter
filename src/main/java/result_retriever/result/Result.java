package result_retriever.result;

public record Result(String resultStatus, String errorMessage, Object content) {

    @Override
    public String toString() {

        if (resultStatus.equals(ResultStatus.OK)) {
            return content.toString();
        } else {
            return "Error: " + errorMessage;
        }

    }

}
