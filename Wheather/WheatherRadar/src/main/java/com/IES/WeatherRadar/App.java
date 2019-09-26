package com.IES.WeatherRadar;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Response;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {   int cityCode=0;
        if(args.length==0){
            System.err.println("Por favor indique o codigo da cidade!");
            System.exit(0);
        } else {
            cityCode=Integer.parseInt(args[0]);
        }
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://api.ipma.pt/open-data/")
            .addConverterFactory(GsonConverterFactory. create ())
            .build();
        IpmaService service = retrofit.create(IpmaService.class);

        // get city code by the name
        if(args.length == 2) {
            Call<IpmaCities> callSyncCities = service.getAllCities();
            try {
                Response<IpmaCities> apiResponseCities = callSyncCities.execute();
                IpmaCities allCities = apiResponseCities.body();
                for(City c : allCities.getData()) {
                    if(c.getLocal().equals(args[1]))
                        cityCode = c.getGlobalIdLocal();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("City code " + cityCode);
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
            System.exit(0);

                
        } catch (Exception ex) {
        ex.printStackTrace();
        }
            }
}
