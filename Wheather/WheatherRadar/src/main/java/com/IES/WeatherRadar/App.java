package com.IES.WeatherRadar;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Hello world!
 *
 */
public class App 
{   
    private static final Logger LOGGER = LogManager.getLogger(App.class.getName());
    public static void main( String[] args )
    {   int cityCode=0;
        LOGGER.debug("Debug log message");
        LOGGER.info("Info log message");
        LOGGER.error("Error log message");
        if(args.length==0){
            System.err.println("Por favor indique o codigo da cidade!");
            LOGGER.error("Error getting city code");

            System.exit(0);
        }else{
            cityCode=Integer.parseInt(args[0]);
            LOGGER.info("City code:"+cityCode);

        }
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://api.ipma.pt/open-data/")
            .addConverterFactory(GsonConverterFactory. create ())
            .build();
        IpmaService service = retrofit.create(IpmaService.class);
        Call<IpmaCityForecast> callSync = service.getForecastForACity( cityCode );
        try {
            Response<IpmaCityForecast> apiResponse = callSync.execute();
            IpmaCityForecast forecast = apiResponse.body();
            String tempMax=forecast.getData().listIterator().next().getTMax();
            String tempMin=forecast.getData().listIterator().next().getTMin();
            String longitude=forecast.getData().listIterator().next().getLongitude();
            String latitude=forecast.getData().listIterator().next().getLatitude();
            String format = "%-20s%-20s%-20s%s%n";
            System.out.printf(format,"Latitude","Longitude","Max Temp","Min Temp");
            System.out.printf(format,latitude,longitude,tempMax,tempMin);
            LOGGER.info("Weather for city with code: "+cityCode+"=> Latitude:"+ latitude+", Longitude:"+ longitude+", temperatura máxima:"+ tempMax+", temperatura minima"+ tempMin);

            System.exit(0);

                
        } catch (Exception ex) {
        ex.printStackTrace();
        LOGGER.error("Error getting forecast");
        }
    }
}
