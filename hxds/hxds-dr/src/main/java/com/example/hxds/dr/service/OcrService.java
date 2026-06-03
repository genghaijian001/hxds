package com.example.hxds.dr.service;

import java.util.Map;

public interface OcrService {

    Map<String, String> ocrIdCard(String base64Image, String cardSide);

    Map<String, String> ocrDriverLicense(String base64Image);
}
