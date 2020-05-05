package com.aperto.magkit.monitoring.endpoint.scheduler;

/**
 * 
 * This pojo is used to provide information about the recurring jobs configured
 * in Magnolia.
 * 
 * @author MIHAELA PAPARETE (IBM)
 * @since 2020-04-23
 *
 */
public class JobInfo {

    private String _name;
    private String _description;
    private String _cron;
    private String _nextExecution;

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        _description = description;
    }

    public String getCron() {
        return _cron;
    }

    public void setCron(String cron) {
        _cron = cron;
    }

    public String getNextExecution() {
        return _nextExecution;
    }

    public void setNextExecution(String nextExecution) {
        _nextExecution = nextExecution;
    }
}
