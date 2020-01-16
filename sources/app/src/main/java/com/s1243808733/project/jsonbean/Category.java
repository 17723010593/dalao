package com.s1243808733.project.jsonbean;

import java.util.List;

public class Category {

	private String icon;

    private String title;

    private String describes;

    private List<Templates> templates;

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }

    public String getDescribes() {
        return describes;
    }

    public void setTemplates(List<Templates> templates) {
        this.templates = templates;
    }

    public List<Templates> getTemplates() {
        return templates;
    }

    public static class Templates {

        private String icon;

        private String title;

        private String describes;

        private Project project;

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getIcon() {
            return icon;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setDescribes(String describes) {
            this.describes = describes;
        }

        public String getDescribes() {
            return describes;
        }

        public void setProject(Project project) {
            this.project = project;
        }

        public Project getProject() {
            return project;
        }

    }

}

