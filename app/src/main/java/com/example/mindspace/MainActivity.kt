package com.example.mindspace

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    lateinit var speechRecognizer: SpeechRecognizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply{
            // putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
            // putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName())
            // putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR") }

        val textView = findViewById(R.id.sttResult) as TextView
        val sttBtn = findViewById(R.id.sttStart) as Button
        sttBtn.setOnClickListener {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
            speechRecognizer.setRecognitionListener(object: RecognitionListener{
                override fun onReadyForSpeech(params: Bundle?) {}
                // 음성 인식 준비 완료
                override fun onRmsChanged(rmsdB: Float) {}
                // 음성의 RMS가 바뀌었을 때
                override fun onBufferReceived(buffer: ByteArray?) {}
                // 음성 데이터의 buffer를 받을 수 있다.
                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
                override fun onBeginningOfSpeech() {}
                // 사용자가 말하기 시작할 때
                override fun onEndOfSpeech() {}
                // 사용자의 말이 끝났을 때
                override fun onError(error: Int) {
                    var message: String? = null
                    when (error) {
                        SpeechRecognizer.ERROR_AUDIO -> message = "ERROR_AUDIO"
                        SpeechRecognizer.ERROR_CLIENT -> message = "ERROR_CLIENT"
                        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> message = "ERROR_INSUFFICIENT_PERMISSIONS"
                        SpeechRecognizer.ERROR_NETWORK -> message = "ERROR_NETWORK"
                        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> message = "ERROR_NETWORK_TIMEOUT"
                        SpeechRecognizer.ERROR_NO_MATCH -> message = "ERROR_NO_MATCH"
                        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> message = "ERROR_RECOGNIZER_BUSY"
                        SpeechRecognizer.ERROR_SERVER -> message = "ERROR_SERVER"
                        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> message = "ERROR_SPEECH_TIMEOUT"
                    }
                    Toast.makeText(
                            getApplicationContext(),
                            message,
                            Toast.LENGTH_SHORT
                    ).show()
                }
                // 오류가 발생했을 때
                override fun onResults(results: Bundle) {
                    // onResults에서 텍스트를 받는 방법
                    val key = SpeechRecognizer.RESULTS_RECOGNITION
                    val result = results.getStringArrayList(key)
                    //result는 arraylist 안에 들어간 데이터는
                    //0번째가 부드럽게 변형 된 데이터
                    //이 후의 데이터는 띄어쓰기나 맞춤법이 어색한 데이터
                    textView.setText(result!![0])
                }
                // 결과 값을 받음!

            })
            speechRecognizer.startListening(intent) // 인식 시작
            // speechRecognizer.stopListening // 인식 중단
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
    }
}