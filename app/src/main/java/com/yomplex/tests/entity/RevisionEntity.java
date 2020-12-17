package com.yomplex.tests.entity;




public class RevisionEntity {




    public String pdfVersion ="";

    public String documentId;




    public String getPdfVersion() {
        return pdfVersion;
    }

    public void setPdfVersion(String pdfVersion) {
        this.pdfVersion = pdfVersion;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    @Override
    public String toString() {
        return "RevisionEntity{" +

                " version='" + pdfVersion + '\'' +
                ", doc_id='" + documentId + '\'' +

                '}';
    }
}
