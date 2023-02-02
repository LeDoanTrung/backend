package com.shoppingcart.admin.user.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shoppingcart.admin.AbstractExporter;
import com.shoppingcart.admin.entity.Category;
import com.shoppingcart.admin.entity.User;

public class CategoryCsvExporter extends AbstractExporter{
	public void export(List<Category> listCategories, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv", ".csv", "categories_");
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), 
				CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader = {"ID", "Name", "Alias", "Enabled"};
		String[] fieldMapping = {"id", "name", "alias", "enabled"};
		csvWriter.writeHeader(csvHeader);
		
		for (Category category : listCategories) {
			csvWriter.write(category, fieldMapping);
		}
		
		csvWriter.close();
	}
}
