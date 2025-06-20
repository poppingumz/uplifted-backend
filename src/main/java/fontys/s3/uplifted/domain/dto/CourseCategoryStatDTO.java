package fontys.s3.uplifted.domain.dto;

import fontys.s3.uplifted.domain.enums.InterestCategory;

public record CourseCategoryStatDTO(InterestCategory category, long courseCount) {}
