package by.teran.transactions.entity;


public record UserBalance(String email, long balance) {
    public static UserBalance fromCsv(String csvRecord) {
        final String[] fields = csvRecord.split(",");
        assert fields.length == 2;
        return new UserBalance(fields[0], Long.parseLong(fields[1]));
    }
}
