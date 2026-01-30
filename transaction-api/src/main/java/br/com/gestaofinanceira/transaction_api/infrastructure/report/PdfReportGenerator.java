package br.com.gestaofinanceira.transaction_api.infrastructure.report;


public class PdfReportGenerator {

//    @Override
//    public byte[] generate(List<Transaction> transactions) {
//        StringBuilder html = new StringBuilder("""
//            <html>
//            <h2>Transaction Report</h2>
//            <table border="1" width="100%">
//            <tr>
//                <th>Date</th><th>Type</th><th>Category</th>
//                <th>Amount</th><th>Status</th>
//            </tr>
//        """);
//
//        for (Transaction t : transactions) {
//            html.append("""
//                <tr>
//                    <td>%s</td>
//                    <td>%s</td>
//                    <td>%s</td>
//                    <td>%s</td>
//                    <td>%s</td>
//                </tr>
//            """.formatted(
//                    t.getCreatedAt(),
//                    t.getType(),
//                    t.getCategory(),
//                    t.getOriginalAmount().getAmount(),
//                    t.getStatus()
//            ));
//        }
//
//        html.append("</table></html>");
//
//        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//            HtmlConverter.convertToPdf(html.toString(), out);
//            return out.toByteArray();
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to generate PDF report", e);
//        }
//    }
}

