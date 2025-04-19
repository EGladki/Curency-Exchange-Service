package com.gladkiei.exchanger.utils;

import com.gladkiei.exchanger.exception.BadRequestException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ParserUtil {
    public String extractRateFromRequest(HttpServletRequest req) throws IOException {
        try (Scanner scanner = new Scanner(req.getInputStream())) {
            if (scanner.hasNext()) {
                String data = scanner.next();
                String[] parts = data.split("=");
                if (parts.length != 2) {
                    throw new BadRequestException("Rate parameter value is missing");
                }
                String decoded = java.net.URLDecoder.decode(parts[1], StandardCharsets.UTF_8);
                return decoded.replace(",", ".");
            }
        }
        throw new BadRequestException("Rate parameter is missing");
    }
}
