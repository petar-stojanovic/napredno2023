//package src.kol_2.zadnji_20;
//import java.util.*;
//
//// 34
//public class LogsTester {
//    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        LogCollector collector = new LogCollector();
//        while (sc.hasNextLine()) {
//            String line = sc.nextLine();
//            if (line.startsWith("addLog")) {
//                collector.addLog(line.replace("addLog ", ""));
//            } else if (line.startsWith("printServicesBySeverity")) {
//                collector.printServicesBySeverity();
//            } else if (line.startsWith("getSeverityDistribution")) {
//                String[] parts = line.split("\\s+");
//                String service = parts[1];
//                String microservice = null;
//                if (parts.length == 3) {
//                    microservice = parts[2];
//                }
//                collector.getSeverityDistribution(service, microservice).forEach((k,v)-> System.out.printf("%d -> %d%n", k,v));
//            } else if (line.startsWith("displayLogs")){
//                String[] parts = line.split("\\s+");
//                String service = parts[1];
//                String microservice = null;
//                String order = null;
//                if (parts.length == 4) {
//                    microservice = parts[2];
//                    order = parts[3];
//                } else {
//                    order = parts[2];
//                }
//                collector.displayLogs(service, microservice, order);
//            }
//        }
//    }
//}