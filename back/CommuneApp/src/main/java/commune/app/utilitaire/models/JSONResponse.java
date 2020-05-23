/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commune.app.utilitaire.models;

/**
 *
 * @author Mirantolalaina
 */
public class JSONResponse {
    private String status;
    private int code;
    private String message; 
    private Object[] response;

    public JSONResponse(String status, int code, String message, Object[] response) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.response = response;
    }
    
    public JSONResponse(String status, int code, String message, Object response) {
        this.status = status;
        this.code = code;
        this.message = message;
        Object[] o=new Object[1];
        o[0]=response;
        this.response = o;
    }

    public JSONResponse() {
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

    public Object[] getResponse() {
        return response;
    }

    public void setResponse(Object[] response) {
        this.response = response;
    }
    
    public void setResponse(Object response) {
        Object[] o=new Object[1];
        o[0]=response;
        this.response = o;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
        this.status=getStatusMessage(code);
    }
    
    private String getStatusMessage(int code) {
        switch (code) {
            case 100:
                return "Continue";  //Attente de la suite de la requête
            case 200:
                return "OK";
            case 404:
                return "Not Found";
            case 403:
                //Refus de traitement de la requête
                return "Forbidden";
            case 500:
                return "Internal Server Error";
            default:
                return null;
        }
    }
    
}
