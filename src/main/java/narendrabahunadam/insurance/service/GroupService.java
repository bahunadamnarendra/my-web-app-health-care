package narendrabahunadam.insurance.service;

import narendrabahunadam.insurance.entity.Group;
import narendrabahunadam.insurance.entity.Plan;
import narendrabahunadam.insurance.repository.GroupRepository;
import narendrabahunadam.insurance.repository.PlanRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private PlanRepository planRepository;

    public void processExcel(MultipartFile file) throws Exception {
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Map<String, Group> groupMap = new HashMap<>();

            // Parse Groups sheet
            Sheet groupSheet = workbook.getSheet("Groups");
            if (groupSheet != null) {
                for (Row row : groupSheet) {
                    if (row.getRowNum() == 0) continue; // Skip header

                    String name = getCellValue(row.getCell(0));
                    String code = getCellValue(row.getCell(1));
                    String description = getCellValue(row.getCell(2));

                    if (!groupMap.containsKey(code)) {
                        Group group = new Group();
                        group.setName(name);
                        group.setCode(code);
                        group.setDescription(description);
                        groupMap.put(code, group);
                    }
                }
            }

            // Save groups first
            groupRepository.saveAll(groupMap.values());

            // Parse Plans sheet
            Sheet planSheet = workbook.getSheet("Plans");
            if (planSheet != null) {
                List<Plan> plans = new ArrayList<>();

                for (Row row : planSheet) {
                    if (row.getRowNum() == 0) continue;

                    String groupCode = getCellValue(row.getCell(0));
                    String planType = getCellValue(row.getCell(1));
                    double coverage = Double.parseDouble(getCellValue(row.getCell(2)));
                    double premium = Double.parseDouble(getCellValue(row.getCell(3)));

                    Group group = groupMap.get(groupCode);
                    if (group != null) {
                        Plan plan = new Plan();
                        plan.setType(planType);
                        plan.setCoverageAmount(coverage);
                        plan.setMonthlyPremium(premium);
                        plan.setGroup(group);
                        plans.add(plan);
                    }
                }

                planRepository.saveAll(plans);
            }
        }
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        } else {
            return cell.getStringCellValue().trim();
        }
    }
}