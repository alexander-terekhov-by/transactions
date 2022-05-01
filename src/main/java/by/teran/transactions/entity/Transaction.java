package by.teran.transactions.entity;


public record Transaction(String name,
                          String surname,
                          String email,
                          long amount, //BigInt is better as it is money
                          String id) {

    public static Transaction fromCsv(String csvRecord) {
        final String[] fields = csvRecord.split(",");
        assert fields.length == 5;
        return new Transaction(fields[0], fields[1], fields[2], Long.parseLong(fields[3]), fields[4]);
    }
}
