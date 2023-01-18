package com.example.myapplication;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.theokanning.openai.completion.CompletionRequest;

class OpenAiApiExample {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void main(String... args) {
        String token = System.getenv("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");
        OpenAiService service = new OpenAiService(token);

        System.out.println("\nCreating completion...");
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model("ada")
                .prompt("Somebody once told me the world is gonna roll me")
                .echo(true)
                .user("testing")
                .build();
        service.createCompletion(completionRequest).getChoices().forEach(System.out::println);
    }
}
