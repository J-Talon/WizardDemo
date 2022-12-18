package me.camm.productions.wizardrydemo.Spells.Util;

public abstract class SpellData<T> {

    protected T data;

    public T getData(){
        return data;
    }

    public SpellData(T object){
        this.data = object;
    }
}
