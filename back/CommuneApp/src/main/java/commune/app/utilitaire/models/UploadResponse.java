/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commune.app.utilitaire.models;

/**
 *
 * @author P11A-MANOHISOA
 */
public class UploadResponse {
    private String status;
   // private int code;
    private String message; 
    private String url;

    public UploadResponse(String status, String message, String url) {
        this.status = status;
        this.message = message;
        this.url = url;
    }

    public UploadResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
