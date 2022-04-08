import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RainfallReport {
	
	public boolean validate(String pinCode) throws InvalidCityPincodeException {
		
		if(pinCode.length() == 5)
			return true;
		else
			throw new InvalidCityPincodeException("Invalid Pincode");
	}
	
	@SuppressWarnings("resource")
	public List<AnnualRainfall> generateRainfallReport(String filePath) throws NumberFormatException, IOException{
		
		List<AnnualRainfall> avgList = new ArrayList<>();
		
		File f = new File(filePath);
		FileReader fr = new FileReader(f);
		
		BufferedReader br = new BufferedReader(fr);
		
		String 	l;
		
		while((l=br.readLine()) != null) {
			
			String[] arr = l.split(",");
			String pinCode = arr[0];
			
			// check that the pincode is valid or not 
			
			try {
				
				if(validate(pinCode)) {
					
					double[] monthlyRainfall = new double[12];
					
					for(int i=2;i<=13;i++) {
						monthlyRainfall[i-2] = Double.parseDouble(arr[i]);
					}
					
					AnnualRainfall ar = new AnnualRainfall();
					ar.calculateAverageAnnualRainfall(monthlyRainfall);
					ar.setCityName(arr[1]);
					ar.setCityPincode(Integer.parseInt(pinCode));
					
					avgList.add(ar);
				}
				
				
			}catch(InvalidCityPincodeException e) {
				System.out.println(e.getMessage());
			}
			br.close();
			return avgList;
			
		}
		return avgList;
		
	}
	
	
	public List<AnnualRainfall> findMaximumRainfallCities() throws SQLException, IOException, ClassNotFoundException {
		
			DBHandler d = new DBHandler();
			List<AnnualRainfall> finalList = new ArrayList<>();
			
			Connection conn = d.establishConnection();
			Statement st = conn.createStatement();
			
			String sql = "select * from annualrainfall  where average_annual_rainfall in (select max(average_annual_rainfall) from annualrainfall)";
			ResultSet rs = st.executeQuery(sql);
			
			while(rs.next()){
				
				AnnualRainfall ar= new AnnualRainfall();
				
				ar.setCityName(rs.getString(2));
				ar.setCityPincode(Integer.parseInt(rs.getString(1)));
				ar.setAverageAnnualRainfall(Double.parseDouble(rs.getString(3)));
				
				finalList.add(ar);
			}
			
			return finalList;
	}
}
