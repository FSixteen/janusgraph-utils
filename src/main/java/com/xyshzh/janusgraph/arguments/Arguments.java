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
      schemaOptions = new SchemaOptions(this.commonOptions.getOptions());
      if (!schemaOptions.check()) {
        this.schemaOptions.printError();
        System.out.println("--------------------------------------------------\n");
        this.schemaOptions.printRole();
        System.exit(1);
      }
    } else if (commonOptions.isImport()) {
      importOptions = new ImportOptions(this.commonOptions.getOptions());
      if (!importOptions.check()) {
        this.importOptions.printError();
        System.out.println("--------------------------------------------------\n");
        this.importOptions.printRole();
        System.exit(1);
      }
    } else if (commonOptions.isExport()) {
      exportOptions = new ExportOptions(this.commonOptions.getOptions());
      if (!exportOptions.check()) {
        this.exportOptions.printError();
        System.out.println("--------------------------------------------------\n");
        this.exportOptions.printRole();
        System.exit(1);
      }
    } else {
      System.exit(1);
    }
  }

  public boolean isSchema() {
    return commonOptions.isSchema();
  }

  public boolean isImport() {
    return commonOptions.isImport();
  }

  public boolean isExport() {
    return commonOptions.isExport();
  }

  public SchemaOptions getSchemaOptions() {
    return schemaOptions;
  }

  public ImportOptions getImportOptions() {
    return importOptions;
  }

  public ExportOptions getExportOptions() {
    return exportOptions;
  }

}
