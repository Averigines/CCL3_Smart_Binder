package com.example.myapplication

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.theokanning.openai.OpenAiResponse
import com.theokanning.openai.completion.CompletionRequest
//openai-java/client/src/main/java/com/theokanning/openai/OpenAiService.java
//openai-java/api/src/main/java/com/theokanning/openai/completion/CompletionRequest.java

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    //val service = OpenAiService("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")
    val completionRequest = CompletionRequest.builder()
        .prompt("hello")
        .model("ada")
        .echo(true)
        .build()
    //service.createCompletion(completionRequest).getChoices().forEach(System.ou::println


}
