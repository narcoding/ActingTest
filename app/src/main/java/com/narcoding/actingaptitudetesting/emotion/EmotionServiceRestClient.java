//
// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license.
//
// Microsoft Cognitive Services (formerly Project Oxford): https://www.microsoft.com/cognitive-services
//
// Microsoft Cognitive Services (formerly Project Oxford) GitHub:
// https://github.com/Microsoft/Cognitive-Emotion-Android
//
// Copyright (c) Microsoft Corporation
// All rights reserved.
//
// MIT License:
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to
// the following conditions:
//
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED ""AS IS"", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//
package com.narcoding.actingaptitudetesting.emotion;

import com.google.gson.Gson;
import com.microsoft.projectoxford.face.common.RequestMethod;
import com.microsoft.projectoxford.face.rest.ClientException;
import com.microsoft.projectoxford.face.rest.WebServiceRequest;
import com.narcoding.actingaptitudetesting.emotion.contract.FaceRectangle;
import com.narcoding.actingaptitudetesting.emotion.contract.RecognizeResult;
import com.narcoding.actingaptitudetesting.emotion.rest.EmotionServiceException;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmotionServiceRestClient implements EmotionServiceClient {
    //private static final String DEFAULT_API_ROOT = "https://westus.api.cognitive.microsoft.com/emotion/v1.0";
    private static final String DEFAULT_API_ROOT = "https://myfaceapinaim.cognitiveservices.azure.com/face/v1.0/";
    private static final String FACE_RECTANGLES = "faceRectangles";
    private final String apiRoot;
    private final WebServiceRequest restCall;
    private Gson gson = new Gson();

    public EmotionServiceRestClient(String subscriptionKey) {
        this(subscriptionKey, DEFAULT_API_ROOT);
    }

    public EmotionServiceRestClient(String subscriptionKey, String apiRoot) {
        this.apiRoot = apiRoot.replaceAll("/$", "");
        this.restCall = new WebServiceRequest(subscriptionKey);
    }

    @Override
    public List<RecognizeResult> recognizeImage(String url) throws EmotionServiceException {
        return recognizeImage(url, null);
    }

    @Override
    public List<RecognizeResult> recognizeImage(String url, FaceRectangle[] faceRectangles) throws EmotionServiceException {
        Map<String, Object> params = new HashMap<>();
        String path = apiRoot + "/recognize";
        if (faceRectangles != null && faceRectangles.length > 0) {
            params.put(FACE_RECTANGLES, getFaceRectangleStrings(faceRectangles));
        }
        String uri = WebServiceRequest.getUrl(path, params);

        params.clear();
        params.put("url", url);

        String json = null;
        try {
            json = (String) this.restCall.request(uri, RequestMethod.POST, params, null);
        } catch (ClientException | IOException e) {
            e.printStackTrace();
        }
        RecognizeResult[] recognizeResult = this.gson.fromJson(json, RecognizeResult[].class);

        return Arrays.asList(recognizeResult);
    }

    @Override
    public List<RecognizeResult> recognizeImage(InputStream stream) throws EmotionServiceException, IOException {
        return recognizeImage(stream, null);
    }

    @Override
    public List<RecognizeResult> recognizeImage(InputStream stream, FaceRectangle[] faceRectangles) throws EmotionServiceException, IOException {
        Map<String, Object> params = new HashMap<>();
        String path = apiRoot + "/recognize";
        if (faceRectangles != null && faceRectangles.length > 0) {
            params.put(FACE_RECTANGLES, getFaceRectangleStrings(faceRectangles));
        }

        String uri = WebServiceRequest.getUrl(path, params);

        params.clear();



        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = stream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();

        data = buffer.toByteArray();


        //byte[] data = IOUtils.toByteArray(stream);
        params.put("data", data);

        String json = null;
        try {
            json = (String) this.restCall.request(uri, RequestMethod.POST, params, "application/octet-stream");
        } catch (ClientException e) {
            e.printStackTrace();
        }
        RecognizeResult[] recognizeResult = this.gson.fromJson(json, RecognizeResult[].class);

        return Arrays.asList(recognizeResult);
    }

    private String getFaceRectangleStrings(FaceRectangle[] faceRectangles) {
        StringBuffer sb = new StringBuffer();

        boolean firstRectangle = true;
        for (FaceRectangle faceRectangle : faceRectangles) {
            if (firstRectangle) {
                firstRectangle = false;
            } else {
                sb.append(';');
            }
            sb.append(String.format("%d,%d,%d,%d", faceRectangle.left, faceRectangle.top, faceRectangle.width, faceRectangle.height));
        }
        return sb.toString();
    }
}
