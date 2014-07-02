package nl.finan.jbehave.dao.impl;

public class NamedParameter
{
	private String name;

	private Object value;

	public void setValue(Object value)
	{
		this.value = value;
	}

	public Object getValue()
	{
		return value;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public static NamedParameter np(String name, Object value)
	{
		NamedParameter np = new NamedParameter();
		np.setName(name);
		np.setValue(value);

		return np;
	}

	@Override
	public String toString()
	{
		return String.format("%s = %s", name, value);
	}
}