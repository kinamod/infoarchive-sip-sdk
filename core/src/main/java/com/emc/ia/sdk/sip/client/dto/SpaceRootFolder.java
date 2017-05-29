/*
 * Copyright (c) 2016-2017 by OpenText Corporation. All Rights Reserved.
 */
package com.emc.ia.sdk.sip.client.dto;

public class SpaceRootFolder extends NamedLinkContainer {

  private String fileSystemRoot;

  public String getFileSystemRoot() {
    return fileSystemRoot;
  }

  public void setFileSystemRoot(String fileSystemRoot) {
    this.fileSystemRoot = fileSystemRoot;
  }

}
