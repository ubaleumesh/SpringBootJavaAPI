package com.swnirmit.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.swnirmit.ety.EtyUsersMaster;

public class eMailBody {

	public String getRegSuccessMailBody(EtyUsersMaster userAc) {
		String fileName = "unlock-acc-email-body.txt";
		List<String> replaceLines = null;
		String mailBody = null;
		try {
			Path path = Paths.get(fileName, "");
			Stream<String> lines = Files.lines(path);
			replaceLines = lines
					.map(line -> line.replace("{FNAME}", userAc.getFristName()).replace("{LNAME}", userAc.getLastName())
							.replace("{TEMP-PWD}", userAc.getUserPassword()).replace("{EMAIL}", userAc.getUserMailId()))
					.collect(Collectors.toList());
			mailBody = String.join("", replaceLines);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mailBody;

	}

}
