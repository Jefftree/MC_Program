package mc.program;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Activation
{
    String connectionStatus;
    Boolean activated = false;   
    String failureStatus = "";
    
    // HTTP GET request
    public Activation(String key) throws Exception
    {

        String activationKey = key; // user's activation key

        String url = "http://jying.ca/proj/mc/checkcode.php?key=" + activationKey; // checks my site to see if the code is valid

        //Don't worry about these 5 lines
        String USER_AGENT = "Mozilla/5.0"; //pretend java is a legit web browser
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);

        //determines the connection status 
        connectionStatus = getResponseCode(con.getResponseCode());

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine = in.readLine();
        in.close(); // close file

        try
        {
            if (inputLine.trim().equals("Y"))
            {
                activated = true;
            }
            else
            {
                activated = false;                //rip
                failureStatus += " Invalid key.";
            }
        }
        catch (Exception e)
        {
            // If website has problems, assume activation failed
            activated = false;
            failureStatus += "Bad internet connection";
        }

    }

    //If 200 is returned, no connection problems, else there may be a connection problem
    private static String getResponseCode(int i)
    {
        String code;
        switch (i)
        {
            case 100:
                code = "Continue";
                break;
            case 101:
                code = "Switching Protocols";
                break;
            case 200:
                code = "OK";
                break;
            case 201:
                code = "Created";
                break;
            case 202:
                code = "Accepted";
                break;
            case 203:
                code = "Non-Authoritative Information";
                break;
            case 204:
                code = "No Content";
                break;
            case 205:
                code = "Reset Content";
                break;
            case 206:
                code = "Partial Content";
                break;
            case 300:
                code = "Multiple Choices";
                break;
            case 301:
                code = "Moved Permanently";
                break;
            case 302:
                code = "Moved Temporarily";
                break;
            case 303:
                code = "See Other";
                break;
            case 304:
                code = "Not Modified";
                break;
            case 305:
                code = "Use Proxy";
                break;
            case 400:
                code = "Bad Request";
                break;
            case 401:
                code = "Unauthorized";
                break;
            case 402:
                code = "Payment Required";
                break;
            case 403:
                code = "Forbidden";
                break;
            case 404:
                code = "Not Found";
                break;
            case 405:
                code = "Method Not Allowed";
                break;
            case 406:
                code = "Not Acceptable";
                break;
            case 407:
                code = "Proxy Authentication Required";
                break;
            case 408:
                code = "Request Time-out";
                break;
            case 409:
                code = "Conflict";
                break;
            case 410:
                code = "Gone";
                break;
            case 411:
                code = "Length Required";
                break;
            case 412:
                code = "Precondition Failed";
                break;
            case 413:
                code = "Request Entity Too Large";
                break;
            case 414:
                code = "Request-URI Too Large";
                break;
            case 415:
                code = "Unsupported Media Type";
                break;
            case 500:
                code = "Internal Server Error";
                break;
            case 501:
                code = "Not Implemented";
                break;
            case 502:
                code = "Bad Gateway";
                break;
            case 503:
                code = "Service Unavailable";
                break;
            case 504:
                code = "Gateway Time-out";
                break;
            case 505:
                code = "HTTP Version not supported";
                break;
            default:
                code = "Unknown Error";
                break;
        }
        return code;
    }
}