package com.xyshzh.janusgraph.arguments;

public class Arguments {

  public static void main(String[] args) {
    new Arguments(args);
  }

  CommonOptions commonOptions = null;

  SchemaOptions schemaOptions = null;

  ImportOptions importOptions = null;

  ExportOptions exportOptions = null;

  public Arguments() {}

  public Arguments(String[] args) {
    this.commonOptions = new CommonOptions(args);
    // 判断通用参数正确性
    if (!this.commonOptions.check()) {
      this.commonOptions.printError();
      System.out.println("--------------------------------------------------\n");
      this.commonOptions.printRole();
      System.exit(1);
    }

    // 根据功能判断功能参数正确性
    if (commonOptions.isSchema()) {
      this.schemaOptions = new SchemaOptions(this.commonOptions.getOptions());
      if (!this.schemaOptions.check()) {
        this.schemaOptions.printError();
        System.out.println("--------------------------------------------------\n");
        this.schemaOptions.printRole();
        System.exit(1);
      }
    } else if (commonOptions.isImport()) {
      this.importOptions = new ImportOptions(this.commonOptions.getOptions());
      if (!this.importOptions.check()) {
        this.importOptions.printError();
        System.out.println("--------------------------------------------------\n");
        this.importOptions.printRole();
        System.exit(1);
      }
    } else if (commonOptions.isExport()) {
      this.exportOptions = new ExportOptions(this.commonOptions.getOptions());
      if (!this.exportOptions.check()) {
        this.exportOptions.printError();
        System.out.println("--------------------------------------------------\n");
        this.exportOptions.printRole();
        System.exit(1);
      }
    } else {
      System.exit(1);
    }
    
    this.commonOptions.printRole();
  }

  public boolean isSchema() {
    return this.commonOptions.isSchema();
  }

  public boolean isImport() {
    return this.commonOptions.isImport();
  }

  public boolean isExport() {
    return this.commonOptions.isExport();
  }

  public CommonOptions getCommonOptions() {
    return this.commonOptions;
  }

  public SchemaOptions getSchemaOptions() {
    return this.schemaOptions;
  }

  public ImportOptions getImportOptions() {
    return this.importOptions;
  }

  public ExportOptions getExportOptions() {
    return this.exportOptions;
  }

}
