package me.bobulo.mine.pdam.feature.skin;

import java.util.Objects;

public class SkinExtracted {

    private String name;
    private String valueBase64;
    private String signatureBase64;
    private String url;

    public SkinExtracted(String name, String valueBase64, String signatureBase64, String url) {
        this.name = name;
        this.valueBase64 = valueBase64;
        this.signatureBase64 = signatureBase64;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValueBase64() {
        return valueBase64;
    }

    public void setValueBase64(String valueBase64) {
        this.valueBase64 = valueBase64;
    }

    public String getSignatureBase64() {
        return signatureBase64;
    }

    public void setSignatureBase64(String signatureBase64) {
        this.signatureBase64 = signatureBase64;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isEmpty() {
        return (valueBase64 == null || valueBase64.isEmpty());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SkinExtracted)) return false;
        SkinExtracted that = (SkinExtracted) o;
        return Objects.equals(name, that.name) &&
          Objects.equals(valueBase64, that.valueBase64) &&
          Objects.equals(signatureBase64, that.signatureBase64) &&
          Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, valueBase64, signatureBase64, url);
    }

    @Override
    public String toString() {
        return "SkinExtracted{" +
          "name='" + name + '\'' +
          ", valueBase64='" + valueBase64 + '\'' +
          ", signatureBase64='" + signatureBase64 + '\'' +
          ", url='" + url + '\'' +
          '}';
    }
}
