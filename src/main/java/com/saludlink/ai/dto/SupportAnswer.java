package com.saludlink.ai.dto;

import java.util.List;

public record SupportAnswer(String reply, boolean answeredFromDocs, List<String> sources) {}
