package com.www.msagraphql.resolver;

import com.www.msagraphql.model.CourseSession;
import com.www.msagraphql.model.CourseSessionFile;
import java.util.List;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class CourseSessionDataResolver {

  private final FileService fileService;

  public CourseSessionDataResolver(FileService fileService) {
    this.fileService = fileService;
  }

  @SchemaMapping(typeName = "CourseSession", field = "files")
  public List<CourseSessionFile> getFiles(CourseSession courseSession) {
    return fileService.findFilesBySessionId(courseSession.getId());
  }
}
