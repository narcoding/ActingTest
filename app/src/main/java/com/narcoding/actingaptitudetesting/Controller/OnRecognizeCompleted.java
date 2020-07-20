package com.narcoding.actingaptitudetesting.Controller;
import com.microsoft.projectoxford.face.contract.Emotion;

public interface OnRecognizeCompleted {
    void onResult(Emotion emotion);
}
