package com.example.jpp.api;

import com.example.jpp.model.Conseil;
import com.example.jpp.model.Question;
import com.example.jpp.model.Reponse;
import com.example.jpp.model.Utilisateur;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("api/questions")
    Call<List<Question>> getAllQuestions();

    @POST("api/reponses")
    Call<Reponse> createReponse(@Body Reponse reponse);

    // Utilisateur endpoints
    @POST("api/utilisateurs")
    Call<Utilisateur> createUtilisateur(@Body Utilisateur utilisateur);

    @GET("api/utilisateurs/email/{email}")
    Call<Utilisateur> getUtilisateurByEmail(@retrofit2.http.Path("email") String email);

    // Conseil endpoints
    @GET("api/conseils")
    Call<List<Conseil>> getAllConseils();

    @GET("api/questionnaires/{id}/questions")
    Call<List<com.example.jpp.model.QuestionnaireQuestion>> getQuestionsByQuestionnaire(
            @retrofit2.http.Path("id") Long id);

    @GET("api/reponses/question/{idQuestion}")
    Call<List<Reponse>> getReponsesByQuestion(@retrofit2.http.Path("idQuestion") Long idQuestion);
}
