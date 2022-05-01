package by.teran.transactions.utils;


import by.teran.transactions.entity.UserBalance;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import java.util.stream.Stream;

public class CsvReaderUtils {

    private CsvReaderUtils() {
        //utility class
    }

    public static <T> Stream<T> readCsv(String fileName, Function<String, T> creator) {
        try (InputStream inputStream = CsvReaderUtils.class.getClassLoader().getResourceAsStream(fileName)) {
            assert inputStream != null;
            String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return Stream.of(content.split("\n"))
                    .skip(1)
                    .map(creator);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
